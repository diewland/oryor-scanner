// Copyright 2018 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package com.diewland.oryorscanner;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.diewland.oryorscanner.helper.TextGraphic;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
import com.diewland.oryorscanner.helper.FrameMetadata;
import com.diewland.oryorscanner.helper.GraphicOverlay;
import com.diewland.oryorscanner.helper.VisionProcessorBase;

import java.io.IOException;
import java.util.List;

/** Processor for the text recognition demo. */
public class TextRecognitionProcessor extends VisionProcessorBase<FirebaseVisionText> {

    private static final String TAG = "TextRecProc";

    private final FirebaseVisionTextRecognizer detector;
    private Context ctx;
    private boolean processing_flag;
    private AlertDialog alert_box;

    public TextRecognitionProcessor(Context ctx) {
        detector = FirebaseVision.getInstance().getOnDeviceTextRecognizer();
        this.ctx = ctx;
        this.processing_flag = false;
    }

    @Override
    public void stop() {
        try {
            detector.close();
        } catch (IOException e) {
            Log.e(TAG, "Exception thrown while trying to close Text Detector: " + e);
        }
    }

    @Override
    protected Task<FirebaseVisionText> detectInImage(FirebaseVisionImage image) {
        return detector.processImage(image);
    }

    @Override
    protected void onSuccess(
        @NonNull FirebaseVisionText results,
        @NonNull FrameMetadata frameMetadata,
        @NonNull GraphicOverlay graphicOverlay) {

        // when loading info, skip success callback
        if(processing_flag){
            return;
        }

        graphicOverlay.clear();
        List<FirebaseVisionText.TextBlock> blocks = results.getTextBlocks();
        for (int i = 0; i < blocks.size(); i++) {
            List<FirebaseVisionText.Line> lines = blocks.get(i).getLines();
            for (int j = 0; j < lines.size(); j++) {
                List<FirebaseVisionText.Element> elements = lines.get(j).getElements();
                for (int k = 0; k < elements.size(); k++) {
                    //GraphicOverlay.Graphic textGraphic = new TextGraphic(graphicOverlay, elements.get(k));
                    //graphicOverlay.add(textGraphic);

                    String patt = "\\d\\d-\\d-\\d\\d\\d\\d\\d-\\d-\\d\\d\\d\\d";
                    String text = elements.get(k).getText();
                    if(text.matches(patt)){

                        // lock process
                        processing_flag = true;

                        // create dialogbox
                        alert_box = createAlertBox();
                        alert_box.setTitle("Scan Result");
                        alert_box.setMessage("Loading...");
                        alert_box.show();

                        // prepare api
                        String api = "http://porta.fda.moph.go.th/FDA_SEARCH_ALL/PRODUCT/FRM_PRODUCT_FOOD.aspx?fdpdtno=%s";
                        String url = String.format(api, text.replaceAll("-", ""));

                        // call api
                        RequestQueue queue = Volley.newRequestQueue(this.ctx);
                        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        alert_box.setMessage(response);
                                        Log.d(TAG, response);
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                alert_box.setMessage(error.toString());
                                Log.d(TAG, error.toString());
                            }
                        });
                        queue.add(stringRequest);
                    }
                }
            }
        }
    }

    private AlertDialog createAlertBox(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(this.ctx)
            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    processing_flag = false;
                }
            });
        return dialog.create();
    }

    @Override
    protected void onFailure(@NonNull Exception e) {
    Log.w(TAG, "Text detection failed." + e);
  }
}
