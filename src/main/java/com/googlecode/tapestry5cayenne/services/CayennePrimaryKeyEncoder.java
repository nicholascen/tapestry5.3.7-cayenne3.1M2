package com.googlecode.tapestry5cayenne.services;

import java.util.List;

import org.apache.cayenne.Persistent;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.ioc.annotations.Marker;

import com.googlecode.tapestry5cayenne.PrimaryKeyEncoder;
import com.googlecode.tapestry5cayenne.annotations.Cayenne;

/**
 * Providers a PrimaryKeyEncoder for Persistent objects.
 * Any page or component wherein a primary key encoder is needed for Persistent objects, 
 * an encoder is conveniently available by doing:
 * @Inject
 * @Cayenne
 * private PrimaryKeyEncoder encoder;
 * @author robertz
 *
 */
@Marker(Cayenne.class)
public class CayennePrimaryKeyEncoder implements PrimaryKeyEncoder<String,Persistent> {
    
    private final ValueEncoder<Persistent> _encoder;
    
    public CayennePrimaryKeyEncoder(@Cayenne ValueEncoder<Persistent> encoder) {
        _encoder = encoder;
    }

    public void prepareForKeys(List<String> arg0) {}

    public String toKey(Persistent dao) {
        return _encoder.toClient(dao);
    }

    public Persistent toValue(String key) {
        return _encoder.toValue(key);
    }

    public Class<String> getKeyType() {
      return String.class;
    }
}
