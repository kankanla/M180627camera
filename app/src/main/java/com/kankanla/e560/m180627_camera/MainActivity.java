package com.kankanla.e560.m180627_camera;


import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.Arrays;

import static android.hardware.camera2.CameraCaptureSession.CaptureCallback;
import static android.hardware.camera2.CameraCaptureSession.StateCallback;


public class MainActivity extends AppCompatActivity {
    private CameraManager manager;
    private SurfaceView view;
    private SurfaceHolder holder;
    private CameraDevice cameraDevice;
    private Surface surface;
    private CameraCaptureSession msession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            step1();
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        msession.close();
    }

    protected void step1() throws CameraAccessException {
        view = (SurfaceView) findViewById(R.id.surfaceView);
        manager = (CameraManager) getSystemService(CAMERA_SERVICE);
        holder = view.getHolder();
        surface = holder.getSurface();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }
        manager.openCamera("1", new CameraDevice.StateCallback() {
            @Override
            public void onOpened(@NonNull CameraDevice camera) {
                cameraDevice = camera;
            }

            @Override
            public void onDisconnected(@NonNull CameraDevice camera) {

            }

            @Override
            public void onError(@NonNull CameraDevice camera, int error) {

            }
        }, null);

        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(final SurfaceHolder holder) {

            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                System.out.println("7777777777777surfaceCreated777777777777");
                try {
                    cameraDevice.createCaptureSession(Arrays.asList(surface), new StateCallback() {
                        @Override
                        public void onConfigured(@NonNull CameraCaptureSession session) {
                            msession = session;
                            try {
                                CaptureRequest captureRequest;
                                CaptureRequest.Builder builder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
                                builder.addTarget(surface);
                                captureRequest = builder.build();

                                session.setRepeatingRequest(captureRequest, new CaptureCallback() {
                                    @Override
                                    public void onCaptureStarted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, long timestamp, long frameNumber) {
                                        super.onCaptureStarted(session, request, timestamp, frameNumber);
                                    }
                                }, null);

                            } catch (CameraAccessException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onConfigureFailed(@NonNull CameraCaptureSession session) {

                        }
                    }, null);
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });
    }
}
