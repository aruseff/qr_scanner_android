package training.ruseff.com.karateqrscanner.models;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class User implements Parcelable {

    @SerializedName("INTERNAL_ID")
    private long internalId;
    @SerializedName("NAME")
    private String name;
    @SerializedName("EXTERNAL_ID")
    private long externalId;

    public User(long internalId, String name, long externalId) {
        this.internalId = internalId;
        this.name = name;
        this.externalId = externalId;
    }

    public long getInternalId() {
        return internalId;
    }

    public void setInternalId(long internalId) {
        this.internalId = internalId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getExternalId() {
        return externalId;
    }

    public void setExternalId(long externalId) {
        this.externalId = externalId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(internalId);
        parcel.writeString(name);
        parcel.writeLong(externalId);
    }

    protected User(Parcel in) {
        internalId = in.readLong();
        name = in.readString();
        externalId = in.readLong();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
