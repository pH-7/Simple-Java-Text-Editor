package com.wire.collab;

import com.wire.bots.sdk.MessageHandlerBase;
import com.wire.bots.sdk.WireClient;
import com.wire.bots.sdk.models.OTMessage;

import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;

/**
 * Created with IntelliJ IDEA.
 * User: dejankovacevic
 * Date: 19/12/17
 * Time: 21:55
 */
public class OTMessageHandler extends MessageHandlerBase {
    private Document document;
    private DocumentListener listener;

    @Override
    public void onOT(WireClient client, OTMessage msg) {
        document.removeDocumentListener(listener);

        try {

            switch (msg.getOperation()) {
                case INSERT:
                    document.insertString(msg.getOffset(), msg.getText(), new SimpleAttributeSet());
                    break;
                case DELETE:
                    document.remove(msg.getOffset(), msg.getLength());
                    break;
            }

            //System.out.printf("%s %d %s %s\n", msg.getOperation(), msg.getOffset(), msg.getText(), msg.getLength());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.addDocumentListener(listener);
        }
    }

    void setDocument(Document document) {
        this.document = document;
    }

    void setListener(DocumentListener listener) {
        this.listener = listener;
    }
}
