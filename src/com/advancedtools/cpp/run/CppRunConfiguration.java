package com.advancedtools.cpp.run;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.*;
import com.intellij.execution.filters.TextConsoleBuilderFactory;
import com.intellij.execution.process.OSProcessHandler;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.runners.ProgramRunner;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.JDOMExternalizable;
import com.intellij.openapi.util.WriteExternalException;
import com.advancedtools.cpp.run.CppRunSettingsEditor;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;

import java.util.StringTokenizer;

/**
 * @author maxim
 * Date: 28.03.2009
 * Time: 19:42:45
 */
public class CppRunConfiguration extends BaseCppConfiguration<CppRunnerParameters> implements RunProfileWithCompileBeforeLaunchOption, LocatableConfiguration {
  private static final String EXECUTABLE_PARAMETERS = "parameters";
  private static final String WORKING_DIR = "workingDir";

  protected CppRunConfiguration(Project project, ConfigurationFactory factory, String name) {
    super(project, factory, name);
  }

  public SettingsEditor<? extends RunConfiguration> getConfigurationEditor() {
    return new CppRunSettingsEditor(getProject());
  }

  public JDOMExternalizable createRunnerSettings(ConfigurationInfoProvider provider) {
    return null;
  }

  public SettingsEditor<JDOMExternalizable> getRunnerSettingsEditor(ProgramRunner runner) {
    return null;
  }

  protected void fillStateCommandLine(GeneralCommandLine commandLine) {
    String executableParameters = myRunnerParameters.getExecutableParameters();
    if (executableParameters != null) {
      StringTokenizer tokenizer = new StringTokenizer(executableParameters, " ");
      // TODO: something in quotes
      while (tokenizer.hasMoreElements()) {
        commandLine.addParameter(tokenizer.nextToken());
      }
    }

    String path = myRunnerParameters.getWorkingDir();
    if (!isEmpty(path)) commandLine.setWorkDirectory(path);
  }

  @Override
  public void writeExternal(Element element) throws WriteExternalException {
    super.writeExternal(element);

    String parameters = myRunnerParameters != null ? myRunnerParameters.getExecutableParameters():null;
    if (parameters != null) element.setAttribute(EXECUTABLE_PARAMETERS, parameters);

    String launchingPath = myRunnerParameters != null ? myRunnerParameters.getWorkingDir():null;
    if (launchingPath != null) element.setAttribute(WORKING_DIR, launchingPath);
  }

  @Override
  public void readExternal(Element element) throws InvalidDataException {
    super.readExternal(element);
    myRunnerParameters.setExecutableParameters(element.getAttributeValue(EXECUTABLE_PARAMETERS));
    myRunnerParameters.setWorkingDir(element.getAttributeValue(WORKING_DIR));
  }

  @NotNull
  public Module[] getModules() {
    return Module.EMPTY_ARRAY;
  }

  public boolean isGeneratedName() {
    return false;
  }

  public String suggestedName() {
    return "Cpp";
  }

  @NotNull
  protected CppRunnerParameters createRunnerParameters() {
    return new CppRunnerParameters();
  }
}