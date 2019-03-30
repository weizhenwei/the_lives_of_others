package com.example.the_lives_of_others;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.content.ContextWrapper;
import android.media.AudioAttributes;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.os.Environment;
import android.util.Log;

import com.example.the_lives_of_others.AudioConfig;


public class AudioUtils {
    private static final String TAG = "AudioRecord";
    private File parentDirectory;
    private FileInputStream is;
    private FileOutputStream os;

    private AudioRecord audioRecord;
    private AudioTrack audioTrack;
    private boolean isRecording;

    public void init() {
        isRecording = false;
    }

    public int startRecord(File fileDirectory) {
        final int minBufferSize = AudioRecord.getMinBufferSize(AudioConfig.AUDIO_SAMPLE_RATE_HZ,
                AudioConfig.AUDIO_CHANNEL, AudioConfig.AUDIO_FORMAT);
        if (minBufferSize == AudioRecord.ERROR_BAD_VALUE || minBufferSize == AudioRecord.ERROR) {
            Log.e(TAG, "can't get minBufferSize");
            return AudioConfig.OP_STATUS_ERROR;
        }

        audioRecord = new AudioRecord(AudioConfig.AUDIO_SOURCE, AudioConfig.AUDIO_SAMPLE_RATE_HZ,
                AudioConfig.AUDIO_CHANNEL, AudioConfig.AUDIO_FORMAT, minBufferSize);
        if (audioRecord == null) {
            Log.e(TAG, "can't new AudioRecord");
            return AudioConfig.OP_STATUS_ERROR;
        }

        final byte audioData[] = new byte[minBufferSize];
        parentDirectory = fileDirectory;
        Log.i(TAG, "FileDirectory: " + parentDirectory);
        final File audioFile = new File(parentDirectory, "audio_test.pcm");
        if (!audioFile.mkdirs()) {
            Log.e(TAG, "Directory not created");
            // return AudioConfig.OP_STATUS_ERROR;
        }
        if (audioFile.exists()) {
            audioFile.delete();
        }

        audioRecord.startRecording();
        Log.i(TAG, "Audio start recording");
        isRecording = true;

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    os = new FileOutputStream(audioFile);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                if (null != os) {
                    while (isRecording) {
                        int read = audioRecord.read(audioData, 0, minBufferSize);
                        if (AudioRecord.ERROR_INVALID_OPERATION != read) {
                            try {
                                String info = "Write audio dat of " + read + " byres";
                                Log.i(TAG, info);
                                os.write(audioData);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    try {
                        Log.i(TAG, "run: close file output stream !");
                        os.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        return AudioConfig.OP_STATUS_OK;
    }

    public void stopRecord() {
        isRecording = false;

        Log.i(TAG, "Audio stop recording");
        if (null != audioRecord) {
            audioRecord.stop();
            audioRecord.release();
            audioRecord = null;
        }
    }

    public void startPlay() {
        int channelConfig = AudioFormat.CHANNEL_OUT_MONO;
        final int minBufferSize = AudioTrack.getMinBufferSize(AudioConfig.AUDIO_SAMPLE_RATE_HZ,
                channelConfig, AudioConfig.AUDIO_FORMAT);

        audioTrack = new AudioTrack(
                new AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build(),
                new AudioFormat.Builder().setSampleRate(AudioConfig.AUDIO_SAMPLE_RATE_HZ)
                        .setEncoding(AudioConfig.AUDIO_FORMAT)
                        .setChannelMask(channelConfig)
                        .build(),
                minBufferSize,
                AudioTrack.MODE_STREAM,
                AudioManager.AUDIO_SESSION_ID_GENERATE);
        audioTrack.play();

        final File audioFile = new File(parentDirectory, "audio_test.pcm");
        try {
            is = new FileInputStream(audioFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    byte[] tempBuffer = new byte[minBufferSize];
                    while (is.available() > 0) {
                        int readCount = is.read(tempBuffer);
                        if (readCount == AudioTrack.ERROR_INVALID_OPERATION ||
                                readCount == AudioTrack.ERROR_BAD_VALUE) {
                            continue;
                        }
                        if (readCount != 0 && readCount != -1) {
                            audioTrack.write(tempBuffer, 0, readCount);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }

    public void stopPlay() {
        if (audioTrack != null) {
            Log.d(TAG, "Stopping");
            audioTrack.stop();
            Log.d(TAG, "Releasing");
            audioTrack.release();
            Log.d(TAG, "Nulling");

            audioTrack = null;
        }
    }

}
