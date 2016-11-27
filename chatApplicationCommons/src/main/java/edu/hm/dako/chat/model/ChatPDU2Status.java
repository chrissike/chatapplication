package edu.hm.dako.chat.model;

public enum ChatPDU2Status {
  // Client nicht eingeloggt
  UNREGISTERED,
  // Client-Login in Arbeit
  REGISTERING,
  // Client eingeloggt
  REGISTERED,
  // Client-Logout in Arbeit
  UNREGISTERING;
}