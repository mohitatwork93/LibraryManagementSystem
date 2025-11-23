package com.mohit.LMS;

import java.time.LocalDate;

public class Book 
{
    private int id;
    private String title;
    private String author;
    private LocalDate releaseDate;
    private boolean isIssued;

    public Book() { }

    public Book(int id, String title, String author, LocalDate releaseDate, boolean isIssued) 
    {
        this.id = id;
        this.title = title;
        this.author = author;
        this.releaseDate = releaseDate;
        this.isIssued = isIssued;
    }

    public Book(int id, String title, String author, LocalDate releaseDate) 
    {
        this(id, title, author, releaseDate, false);
    }

    public int getId() 
    { 
    	return id; 
    }
    public void setId(int id) 
    {
    	this.id = id;
    }

    public String getTitle() 
    {
    	return title;
    }
    public void setTitle(String title) 
    { 
    	this.title = title; 
    }

    public String getAuthor() 
    {
    	return author; 
    }
    public void setAuthor(String author) 
    {
    	this.author = author; 
    }

    public LocalDate getReleaseDate() 
    {
    	return releaseDate; 
    }
    public void setReleaseDate(LocalDate releaseDate) 
    {
    	this.releaseDate = releaseDate; 
    }

    public boolean isIssued() 
    {
    	return isIssued; 
    }
    public void setIssued(boolean issued) 
    {
    	isIssued = issued; 
    }

    @Override
    public String toString() 
    {
        return id + " | " + title + " | " + author + " | " + (releaseDate != null ? releaseDate : "N/A") + " | " + (isIssued ? "Issued" : "Available");
    }
}
