package com.example.the_lives_of_others;

import android.media.AudioFormat;
import android.media.MediaFormat;
import android.media.MediaRecorder;

public class AudioConfig {
    /*
     * audio sample rate;
     */
    public static final int AUDIO_SAMPLE_RATE_HZ=44100;

    /*
     * audio channel, mono or stero;
     */
    public static final int AUDIO_CHANNEL = AudioFormat.CHANNEL_IN_MONO;

    /*
     * audio format;
     */
    public static final int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;

    /*
     * audio source
     */
    public static final int AUDIO_SOURCE = MediaRecorder.AudioSource.MIC;


    public static final int OP_STATUS_OK = 0;
    public static final int OP_STATUS_ERROR = 1;
}

