package com.ontech.com.abgec.Model;

public class intro_ScreenItem {

 String Title, Description;
 int ScreenImg;

 public intro_ScreenItem(String title, String description, int screenImg) {
  Title = title;
  Description = description;
  ScreenImg = screenImg;
 }

 public void setTitle(String title) {
  Title = title;
 }

 public void setDescription(String description) {
  Description = description;
 }

 public void setScreenImg(int screenImg) {
  ScreenImg = screenImg;
 }

 public String getTitle() {
  return Title;
 }

 public String getDescription() {
  return Description;
 }

 public int getScreenImg() {
  return ScreenImg;
 }
}