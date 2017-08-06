package com.steveq.gardenpieclient.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.steveq.gardenpieclient.communication.models.Section;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Adam on 2017-08-06.
 */

public class SectionsRepository implements Repository {
    private static final String TAG = SectionsRepository.class.getSimpleName();
    private SQLiteDatabase database;
    private SectionsSQLiteHelper dbHelper;
    private String[] sectionProjection = {
            SectionsContract.SectionsEntry.COLUMN_SECTION_NUM,
            SectionsContract.SectionsEntry.COLUMN_ACTIVE};

    public SectionsRepository(Context context) {
        dbHelper = SectionsSQLiteHelper.getInstance(context);
    }

    @Override
    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    @Override
    public void close() {
        database.close();
    }

    @Override
    public boolean createSection(Section section) {
        open();

        ContentValues values = new ContentValues();
        values.put(SectionsContract.SectionsEntry.COLUMN_SECTION_NUM, section.getNumber());
        boolean sectionState = section.getActive();
        values.put(SectionsContract.SectionsEntry.COLUMN_ACTIVE, (sectionState) ? 1 : 0);
        long id = database.insert(SectionsContract.SectionsEntry.TABLE_NAME, null, values);

        close();

        if(id > 0){
            return true;
        }
        return false;
    }

    @Override
    public Map<Integer, Section> getSections() {
        open();

        Cursor cursor = database.query(
                SectionsContract.SectionsEntry.TABLE_NAME,
                sectionProjection,
                null,
                null,
                null,
                null,
                null
        );

        Map<Integer, Section> sections = new HashMap<>();
        while(cursor.moveToNext()){
            Section section = new Section();
            section.setNumber(cursor.getInt(cursor.getColumnIndex(SectionsContract.SectionsEntry.COLUMN_SECTION_NUM)));
            int activeStatus = cursor.getInt(cursor.getColumnIndex(SectionsContract.SectionsEntry.COLUMN_ACTIVE));
            section.setActive(activeStatus != 0);
            sections.put(section.getNumber(), section);
        }
        cursor.close();
        close();

        for(int id : sections.keySet()){
            sections.get(id).setTimes(getTimesForSection(id));
            sections.get(id).setDays(getDaysForSection(id));
        }

        Log.d(TAG, "SECTIONS FROM DB : " + sections);
        return sections;
    }

    @Override
    public Section getSectionById(Integer id) throws IllegalStateException {
        open();

        Cursor cursor = database.query(
                SectionsContract.SectionsEntry.TABLE_NAME,
                sectionProjection,
                SectionsContract.SectionsEntry.COLUMN_SECTION_NUM + " = ?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null
        );

        Map<Integer, Section> sections = new HashMap<>();
        while(cursor.moveToNext()){
            Section section = new Section();
            section.setNumber(cursor.getInt(cursor.getColumnIndex(SectionsContract.SectionsEntry.COLUMN_SECTION_NUM)));
            int activeStatus = cursor.getInt(cursor.getColumnIndex(SectionsContract.SectionsEntry.COLUMN_ACTIVE));
            section.setActive(activeStatus == 0);
            sections.put(section.getNumber(), section);
        }
        cursor.close();
        close();

        for(int i : sections.keySet()){
            sections.get(i).setDays(getDaysForSection(i));
        }

        if(sections.size() > 1){
            throw new IllegalStateException("Only one section should be returned from particular ID");
        } else if(sections.size() == 1){
            return sections.values().iterator().next();
        } else {
            return new Section();
        }
    }

    @Override
    public boolean updateSection(Section section) throws IllegalStateException{
        open();

        ContentValues values = new ContentValues();
        boolean sectionState = section.getActive();
        values.put(SectionsContract.SectionsEntry.COLUMN_ACTIVE, (sectionState) ? 1 : 0);
        int affectedRows = database.update(SectionsContract.SectionsEntry.TABLE_NAME,
                                            values,
                                            SectionsContract.SectionsEntry.COLUMN_SECTION_NUM + " = ?",
                                            new String[]{String.valueOf(section.getNumber())});
        close();

        if(affectedRows > 1 ){
            throw new IllegalStateException("Only one section should be affected from particular ID");
        } else {
            return true;
        }
    }

    @Override
    public boolean deleteSection(Integer id) {
        deleteSectionDays(id);
        deleteSectionTimes(id);
        open();
        int affectedRows = database.delete(
                SectionsContract.SectionsEntry.TABLE_NAME,
                SectionsContract.SectionsEntry.COLUMN_SECTION_NUM + "=?",
                new String[]{String.valueOf(id)});
        close();
        if(affectedRows >= 1){
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean createSectionDays(Section section, List<String> days) {
        List<Integer> resultCheck = new ArrayList<>();
        boolean result = true;
        open();
        for(String day : days){
            ContentValues values = new ContentValues();
            values.put(SectionsContract.SectionsDaysEntry.COLUMN_SECTION_ID, section.getNumber());
            values.put(SectionsContract.SectionsDaysEntry.COLUMN_DAY, day);
            resultCheck.add((int)database.insert(SectionsContract.SectionsDaysEntry.TABLE_NAME, null, values));
        }
        section.setDays(days);
        close();
        for(int id : resultCheck){
            if(id < 0){
                result = false;
            }
        }
        return result;
    }

    @Override
    public List<String> getDaysForSection(Integer id) {
        open();

        Cursor cursor = database.query(
                SectionsContract.SectionsDaysEntry.TABLE_NAME,
                new String[]{SectionsContract.SectionsDaysEntry.COLUMN_DAY},
                SectionsContract.SectionsDaysEntry.COLUMN_SECTION_ID + " = ? ",
                new String[]{String.valueOf(id)},
                null,
                null,
                null
        );

        List<String> days = new ArrayList<>();
        while(cursor.moveToNext()){
            days.add(cursor.getString(cursor.getColumnIndex(SectionsContract.SectionsDaysEntry.COLUMN_DAY)));
        }
        cursor.close();
        close();
        return days;
    }

    private class UpdatePackage{
        private List<String> toAdd;
        private List<String> toDelete;

        public UpdatePackage(List<String> toAdd, List<String> toDelete) {
            this.toAdd = toAdd;
            this.toDelete = toDelete;
        }

        public List<String> getToAdd() {
            return toAdd;
        }

        public void setToAdd(List<String> toAdd) {
            this.toAdd = toAdd;
        }

        public List<String> getToDelete() {
            return toDelete;
        }

        public void setToDelete(List<String> toDelete) {
            this.toDelete = toDelete;
        }
    }

    private UpdatePackage getUpdatePackage(List<String> oldDays, List<String> daysToUpdate){
        List<String> newDays = new ArrayList<>();
        for(String dayToUpdate : daysToUpdate){
            int i = 0;
            for(String oldDay : oldDays){
                System.err.println("DAY TO UPDATE : " + dayToUpdate);
                System.err.println("OLD DAY : " + oldDay);
                if(dayToUpdate.equals(oldDay)){
                    oldDays.remove(i);
                    i = -1;
                    break;
                }
                i++;
            }
            if(i != -1){
                newDays.add(dayToUpdate);
            }
        }
        return new UpdatePackage(newDays, oldDays);
    }

    @Override
    public void updateSectionDays(Section section, List<String> days) {
        List<String> currentDays = getDaysForSection(section.getNumber());
        UpdatePackage updatePackage = getUpdatePackage(currentDays, days);

        Log.d(TAG, "TO ADD : " + updatePackage.getToAdd());
        Log.d(TAG, "TO DELETE : " + updatePackage.getToDelete());

        deleteSectionDayEntries(section.getNumber(), updatePackage.getToDelete());
        createSectionDays(section, updatePackage.getToAdd());
    }

    @Override
    public boolean deleteSectionDays(Integer id) throws IllegalStateException{
        open();
        int affectedRows = database.delete(
                SectionsContract.SectionsDaysEntry.TABLE_NAME,
                SectionsContract.SectionsDaysEntry.COLUMN_SECTION_ID + " = ? ",
                new String[]{String.valueOf(id)}
        );
        if(affectedRows > 1 ){
            throw new IllegalStateException("Only one section should be affected from particular ID");
        } else {
            return true;
        }
    }

    @Override
    public boolean deleteSectionDayEntries(Integer id, List<String> days) {
        open();
        boolean deleted = false;
        for(String day : days){
            int affectedRows = database.delete(
                    SectionsContract.SectionsDaysEntry.TABLE_NAME,
                    SectionsContract.SectionsDaysEntry.COLUMN_SECTION_ID + " = ? ",
                    new String[]{String.valueOf(id), day}
            );
            if(affectedRows > 0) deleted = true;
        }
        close();
        return deleted;
    }

    @Override
    public boolean createSectionTimes(Section section, List<String> times) {
        List<Integer> resultCheck = new ArrayList<>();
        boolean result = true;
        open();
        for(String time : times){
            ContentValues values = new ContentValues();
            values.put(SectionsContract.SectionsTimesEntry.COLUMN_SECTION_ID, section.getNumber());
            values.put(SectionsContract.SectionsTimesEntry.COLUMN_TIME, time);
            resultCheck.add((int)database.insert(SectionsContract.SectionsTimesEntry.TABLE_NAME, null, values));
        }
        section.setTimes(times);
        close();
        for(int id : resultCheck){
            if(id < 0){
                result = false;
            }
        }
        return result;
    }

    @Override
    public List<String> getTimesForSection(Integer id) {
        open();

        Cursor cursor = database.query(
                SectionsContract.SectionsTimesEntry.TABLE_NAME,
                new String[]{SectionsContract.SectionsTimesEntry.COLUMN_TIME},
                SectionsContract.SectionsTimesEntry.COLUMN_SECTION_ID + " = ? ",
                new String[]{String.valueOf(id)},
                null,
                null,
                null
        );

        List<String> times = new ArrayList<>();
        while(cursor.moveToNext()){
            times.add(cursor.getString(cursor.getColumnIndex(SectionsContract.SectionsTimesEntry.COLUMN_TIME)));
        }
        cursor.close();
        close();
        return times;
    }

    @Override
    public void updateSectionTimes(Section section, List<String> times) {
        List<String> currentTimes = getTimesForSection(section.getNumber());
        UpdatePackage updatePackage = getUpdatePackage(currentTimes, times);

        Log.d(TAG, "TO ADD : " + updatePackage.getToAdd());
        Log.d(TAG, "TO DELETE : " + updatePackage.getToDelete());

        deleteSectionTimeEntries(section.getNumber(), updatePackage.getToDelete());
        createSectionTimes(section, updatePackage.getToAdd());
    }

    @Override
    public boolean deleteSectionTimes(Integer id) throws IllegalStateException{
        open();
        int affectedRows = database.delete(
                SectionsContract.SectionsTimesEntry.TABLE_NAME,
                SectionsContract.SectionsTimesEntry.COLUMN_SECTION_ID + " = ? ",
                new String[]{String.valueOf(id)}
        );
        if(affectedRows > 1 ){
            throw new IllegalStateException("Only one section should be affected from particular ID");
        } else {
            return true;
        }
    }

    @Override
    public boolean deleteSectionTimeEntries(Integer id, List<String> times) {
        open();
        boolean deleted = false;
        for(String time : times){
            int affectedRows = database.delete(
                    SectionsContract.SectionsTimesEntry.TABLE_NAME,
                    SectionsContract.SectionsTimesEntry.COLUMN_SECTION_ID + " = ? ",
                    new String[]{String.valueOf(id), time}
            );
            if(affectedRows > 0) deleted = true;
        }
        close();
        return deleted;
    }
}
