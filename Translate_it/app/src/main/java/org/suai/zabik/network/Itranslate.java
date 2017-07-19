package org.suai.zabik.network;

import android.widget.TextView;
/**
 * Created by zabik on 28.06.17.
 */

public interface Itranslate {
    void makeRequest(final String text, final String langFrom, final String langTO, final TextView textView) throws InterruptedException;
}
