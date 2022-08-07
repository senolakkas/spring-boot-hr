package com.akkas.hr.model;

public class PublicHoliday {

  private String date;
  private String localName;
  private String name;
  private String countryCode;
  private Boolean fixed;
  private Boolean countyOfficialHoliday;
  private Boolean countyAdministrationHoliday;
  private Boolean global;
  private String[] counties;
  private int launchYear;

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public String getLocalName() {
    return localName;
  }

  public void setLocalName(String localName) {
    this.localName = localName;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getCountryCode() {
    return countryCode;
  }

  public void setCountryCode(String countryCode) {
    this.countryCode = countryCode;
  }

  public Boolean getFixed() {
    return fixed;
  }

  public void setFixed(Boolean fixed) {
    this.fixed = fixed;
  }

  public Boolean getCountyOfficialHoliday() {
    return countyOfficialHoliday;
  }

  public void setCountyOfficialHoliday(Boolean countyOfficialHoliday) {
    this.countyOfficialHoliday = countyOfficialHoliday;
  }

  public Boolean getCountyAdministrationHoliday() {
    return countyAdministrationHoliday;
  }

  public void setCountyAdministrationHoliday(Boolean countyAdministrationHoliday) {
    this.countyAdministrationHoliday = countyAdministrationHoliday;
  }

  public Boolean getGlobal() {
    return global;
  }

  public void setGlobal(Boolean global) {
    this.global = global;
  }

  public String[] getCounties() {
    return counties;
  }

  public void setCounties(String[] counties) {
    this.counties = counties;
  }

  public int getLaunchYear() {
    return launchYear;
  }

  public void setLaunchYear(int launchYear) {
    this.launchYear = launchYear;
  }
}
