package org.jivesoftware.smackx.provider;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import org.jivesoftware.smack.packet.PacketExtension;
import org.jivesoftware.smack.packet.PrivacyItem.PrivacyRule;
import org.jivesoftware.smack.provider.PacketExtensionProvider;
import org.jivesoftware.smackx.packet.DelayInformation;
import org.xmlpull.v1.XmlPullParser;

public class DelayInformationProvider implements PacketExtensionProvider {
    public PacketExtension parseExtension(XmlPullParser parser) throws Exception {
        Date stamp;
        try {
            synchronized (DelayInformation.UTC_FORMAT) {
                stamp = DelayInformation.UTC_FORMAT.parse(parser.getAttributeValue("", "stamp"));
            }
        } catch (ParseException e) {
            try {
                synchronized (DelayInformation.NEW_UTC_FORMAT) {
                    stamp = DelayInformation.NEW_UTC_FORMAT.parse(parser.getAttributeValue("", "stamp"));
                }
            } catch (ParseException e2) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
                stamp = formatter.parse(parser.getAttributeValue("", "stamp"));
            }
        }
        DelayInformation delayInformation = new DelayInformation(stamp);
        delayInformation.setFrom(parser.getAttributeValue("", PrivacyRule.SUBSCRIPTION_FROM));
        delayInformation.setReason(parser.nextText());
        return delayInformation;
    }
}