package com.wire.collab;

import com.wire.bots.sdk.WireClient;
import com.wire.bots.sdk.assets.OT;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * Created with IntelliJ IDEA.
 * User: dejankovacevic
 * Date: 20/12/17
 * Time: 08:44
 */
public class MyDocumentListener implements DocumentListener {
    private final WireClient wireClient;
    private final ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1);


    MyDocumentListener(WireClient wireClient) {
        this.wireClient = wireClient;
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        try {
            int offset = e.getOffset();
            int length = e.getLength();
            String text = e.getDocument().getText(offset, length);
            OT ot = new OT(OT.Operation.INSERT, offset, text);
            sendOverWire(ot);
        } catch (Exception e1) {
            System.err.println(e1.getMessage());
        }
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        int offset = e.getOffset();
        int length = e.getLength();

        OT ot = new OT(OT.Operation.DELETE, offset, length);
        sendOverWire(ot);
    }

    @Override
    public void changedUpdate(DocumentEvent e) {

    }

    private void sendOverWire(OT ot) {
        executorService.execute(() -> {
            try {
                wireClient.sendOT(ot);
            } catch (Exception e1) {
                System.err.println(e1.getMessage());
            }
        });
    }
}
