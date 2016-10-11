package com.jmolsmobile.landscapevideocapture.camera;

import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.view.SurfaceHolder;

import java.io.IOException;

/**
 * Wrapper around the native camera class so all camera access
 * can easily be mocked.
 * <p/>
 * Created by Jeroen Mols on 06/12/15.
 */
public class
NativeCamera {

    private Camera camera = null;
    private Parameters params = null;

    public Camera getNativeCamera() {
        return camera;
    }

    public void openNativeCamera(boolean isFront) throws RuntimeException {
        if (isFront) {
            boolean found = false;
            int i;
            for (i = 0; i < Camera.getNumberOfCameras(); i++) {
                Camera.CameraInfo newInfo = new Camera.CameraInfo();
                Camera.getCameraInfo(i, newInfo);
                if (newInfo.facing == CameraInfo.CAMERA_FACING_FRONT) {
                    found = true;
                    break;
                }
            }

            if (found) camera = Camera.open(CameraInfo.CAMERA_FACING_FRONT);
        } else {
            camera = Camera.open(CameraInfo.CAMERA_FACING_BACK);
        }
    }

    public void unlockNativeCamera() {
        camera.unlock();
    }

    public void releaseNativeCamera() {
        camera.release();
    }

    public void setNativePreviewDisplay(SurfaceHolder holder) throws IOException {
        camera.setPreviewDisplay(holder);
    }

    public void startNativePreview() {
        camera.startPreview();
    }

    public void stopNativePreview() {
        camera.stopPreview();
    }

    public void clearNativePreviewCallback() {
        camera.setPreviewCallback(null);
    }

    public Parameters getNativeCameraParameters() {
        if (params == null) {
            params = camera.getParameters();
        }
        return params;
    }

    public void updateNativeCameraParameters(Parameters params) {
        this.params = params;
        camera.setParameters(params);
    }

    public void setDisplayOrientation(int degrees) {
        camera.setDisplayOrientation(degrees);
    }

    public int getCameraOrientation() {
        CameraInfo camInfo = new CameraInfo();
        Camera.getCameraInfo(getBackFacingCameraId(), camInfo);
        return camInfo.orientation;
    }

    private int getBackFacingCameraId() {
        int cameraId = -1;
        int numberOfCameras = Camera.getNumberOfCameras();
        for (int i = 0; i < numberOfCameras; i++) {
            CameraInfo info = new CameraInfo();
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                cameraId = i;
                break;
            }
        }
        return cameraId;
    }
}
