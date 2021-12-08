package com.copropre.common.utils;

import android.text.TextUtils;

import com.copropre.common.models.CPTask;

import java.text.DateFormat;
import java.util.Collections;
import java.util.Comparator;

public class Utils {

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public final static boolean isValidPassword(CharSequence target) {
        return !TextUtils.isEmpty(target) && target.length() > 7;
    }

    public final static DateFormat dateFormatDdMMHH = DateFormat.getDateInstance(DateFormat.SHORT);

    public final static Comparator<CPTask> sortTaskByDate = new Comparator<CPTask>() {
        public int compare(CPTask t1, CPTask t2) {
            if (t1.getNextDate()== null && t2.getNextDate() == null) {
                return 0;
            }
            if (t1.getNextDate() == null) {
                return 1;
            }
            if (t2.getNextDate() == null) {
                return -1;
            }
            return t1.getNextDate().compareTo(t2.getNextDate());
        }

        };
    };
