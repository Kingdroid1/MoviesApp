package com.company.moviesapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Reviews implements Parcelable {

    public final static Creator <Reviews> CREATOR = new Creator <Reviews> () {


        @SuppressWarnings({
                "unchecked"
        })
        public Reviews createFromParcel(Parcel in) {
            return new Reviews ( in );
        }

        public Reviews[] newArray(int size) {
            return (new Reviews[size]);
        }

    };
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("page")
    @Expose
    private Integer page;
    @SerializedName("results")
    @Expose
    private List <ReviewResult> results = null;
    @SerializedName("total_pages")
    @Expose
    private Integer totalPages;
    @SerializedName("total_results")
    @Expose
    private Integer totalResults;

    protected Reviews(Parcel in) {
        this.id = ((Integer) in.readValue ( (Integer.class.getClassLoader ()) ));
        this.page = ((Integer) in.readValue ( (Integer.class.getClassLoader ()) ));
        in.readList ( this.results, (com.company.moviesapp.model.ReviewResult.class.getClassLoader ()) );
        this.totalPages = ((Integer) in.readValue ( (Integer.class.getClassLoader ()) ));
        this.totalResults = ((Integer) in.readValue ( (Integer.class.getClassLoader ()) ));
    }

    /**
     * No args constructor for use in serialization
     */
    public Reviews() {
    }

    /**
     * @param id
     * @param results
     * @param totalResults
     * @param page
     * @param totalPages
     */
    public Reviews(Integer id, Integer page, List <ReviewResult> results, Integer totalPages, Integer totalResults) {
        super ();
        this.id = id;
        this.page = page;
        this.results = results;
        this.totalPages = totalPages;
        this.totalResults = totalResults;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public List <ReviewResult> getResults() {
        return results;
    }

    public void setResults(List <ReviewResult> results) {
        this.results = results;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(Integer totalResults) {
        this.totalResults = totalResults;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue ( id );
        dest.writeValue ( page );
        dest.writeList ( results );
        dest.writeValue ( totalPages );
        dest.writeValue ( totalResults );
    }

    public int describeContents() {
        return 0;
    }
}
