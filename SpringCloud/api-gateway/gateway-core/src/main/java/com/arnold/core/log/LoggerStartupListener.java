package com.arnold.core.log;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.LoggerContextListener;
import ch.qos.logback.core.Context;
import ch.qos.logback.core.spi.ContextAwareBase;

import java.lang.management.ManagementFactory;

public class LoggerStartupListener extends ContextAwareBase implements LoggerContextListener {
  private boolean started = false;
  private String getPid() {
      return ManagementFactory.getRuntimeMXBean().getName();
  }

  @Override
  public boolean isResetResistant() {
      return false;
  }

  @Override
  public void onStart(LoggerContext loggerContext) {
      if (started) {
          return;
      }
      Context context = getContext();
      context.putProperty("PID", getPid());
      started = true;
  }

  @Override
  public void onReset(LoggerContext loggerContext) {

  }

  @Override
  public void onStop(LoggerContext loggerContext) {

  }

  @Override
  public void onLevelChange(Logger logger, Level level) {

  }
 }