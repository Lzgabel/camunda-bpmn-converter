package cn.lzgabel.camunda.converter.cli;


import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.IVersionProvider;
import picocli.CommandLine.RunLast;

import java.util.concurrent.Callable;

@Command(
        name = "cbc-cli",
        mixinStandardHelpOptions = true,
        versionProvider = CamundaBpmnConverterClient.VersionProvider.class,
        description = "camunda bpmn converter client",
        subcommands = {
                ValidateCommand.class,
                ConvertCommand.class
        }
)
public class CamundaBpmnConverterClient implements Callable<Integer> {
    private static CommandLine cli;

    public static void disableWarning() {
        System.err.close();
        System.setErr(System.out);
    }

    public static void main(String[] args) {
        disableWarning();
        cli =
                new CommandLine(new CamundaBpmnConverterClient())
                        .setExecutionStrategy(new RunLast())
                        .setCaseInsensitiveEnumValuesAllowed(true);
        final int exitcode = cli.execute(args);
        System.exit(exitcode);
    }

    @Override
    public Integer call() {
        cli.usage(System.out);
        return 0;
    }

    static class VersionProvider implements IVersionProvider {
        public String[] getVersion() {
            return new String[]{"cbc v" + CamundaBpmnConverterClient.class.getPackage().getImplementationVersion()};
        }
    }
}
