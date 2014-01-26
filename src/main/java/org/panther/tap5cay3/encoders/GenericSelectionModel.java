package org.panther.tap5cay3.encoders;

//	1-8-2014 - source: https://wiki.apache.org/tapestry/Tapestry5SelectObject

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.OptionGroupModel;
import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.internal.OptionModelImpl;
import org.apache.tapestry5.ioc.services.PropertyAccess;
import org.apache.tapestry5.util.AbstractSelectModel;

public class GenericSelectionModel<T> extends AbstractSelectModel {

       private String labelField;

       private List<T> list;

       private final PropertyAccess adapter;

       public GenericSelectionModel(List<T> list, String labelField, PropertyAccess adapter) {
               this.labelField = labelField;
               this.list = list;
               this.adapter = adapter;
       }

       public List<OptionGroupModel> getOptionGroups() {
               return null;
       }

       public List<OptionModel> getOptions() {
               List<OptionModel> optionModelList = new ArrayList<OptionModel>();
               for (T obj : list) {
                       if (labelField == null) {
                               optionModelList.add(new OptionModelImpl(obj + "", obj));
                       } else {
                               optionModelList.add(new OptionModelImpl(adapter.get(obj, labelField)+"", obj));
                       }
               }
               return optionModelList;
       }
}
