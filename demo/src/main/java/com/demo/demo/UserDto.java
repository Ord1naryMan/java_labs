package com.demo.demo;


public class UserDto {

    private String nameModal;
    private String phoneModal;
    private String emailModal;

    public UserDto() {
    }

    public UserDto(String name, String phone, String email) {
        this.nameModal = name;
        this.phoneModal = phone;
        this.emailModal = email;
    }

    public String getNameModal() {
        return nameModal;
    }

    public void setNameModal(String nameModal) {
        this.nameModal = nameModal;
    }

    public String getPhoneModal() {
        return phoneModal;
    }

    public void setPhoneModal(String phoneModal) {
        this.phoneModal = phoneModal;
    }

    public String getEmailModal() {
        return emailModal;
    }

    public void setEmailModal(String emailModal) {
        this.emailModal = emailModal;
    }

    @Override
    public String toString() {
        return "UserDto{" +
                "name='" + nameModal + '\'' +
                ", phone='" + phoneModal + '\'' +
                ", email='" + emailModal + '\'' +
                '}';
    }
}
