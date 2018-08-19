package com.example.countries;

import android.support.annotation.Nullable;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class ByteArrayToSVGConverter {
    @Nullable
    public static SVG convert(@Nullable byte[] byteArray) {
        if (byteArray != null) {
            InputStream inputStream = new ByteArrayInputStream(byteArray);
            try {
                return SVG.getFromInputStream(inputStream);
            } catch (SVGParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
