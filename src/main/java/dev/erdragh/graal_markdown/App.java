package dev.erdragh.graal_markdown;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Source;
import org.graalvm.polyglot.Value;

/**
 * Sample for how to use unifiedjs as a markdown parser from Java using
 * GraalVM's polyglot API.
 * @author Jan Bayer
 */
public class App {
    public static void main(String[] args) {
        // Setting relevant options
        Map<String, String> options = new HashMap<>();
        // Allow import statements
        options.put("js.commonjs-require", "true");
        // Specify where the node_modules folder and the javascript source are found
        options.put("js.commonjs-require-cwd", "./src_js");
        // since we're not in a browser and also don't have access to native node apis
        // we need to provide replacements for these.
        options.put("js.commonjs-core-modules-replacements", "url:my-url,path:path-browserify,process:my-process");

        // Since Context imlements AutoCloseable we can use its constructor in a try block like this
        // We only allow JS in this context
        try (Context context = Context.newBuilder("js")
                // we need to allow experimental options for js.commonjs-require from above
                .allowExperimentalOptions(true)
                // we need to allow IO access so the Context can access the src_js folder
                .allowIO(true)
                // we supply the options set above
                .options(options)
                .build()) {

            System.out.println("Started reading file");
            long startRead = System.nanoTime();

            String fileContents = Files.lines(Paths.get("daringfireball.md")).collect(Collectors.joining("\n"));

            long durationRead = (System.nanoTime() - startRead) / 1_000_000;
      
            long startContext = System.nanoTime();
            // This string imports the parseMarkdown function from our built source
            // and provides it to the context, not by calling but just by specifying it.
            String src = "import parseMarkdown from './out.mjs'; parseMarkdown";
            // This value stores the actual function, which we can execute. 
            Value parseMarkdown = context.eval(Source.newBuilder("js", src, "start.mjs").build());

            long durationContext = (System.nanoTime() - startContext) / 1_000_000;

            System.out.println("Started to parse markdown");
            // we execute the JS function and print out the results.
            long startParse = System.nanoTime();
            Value result = parseMarkdown.execute(fileContents);
            long durationParse = (System.nanoTime() - startParse) / 1_000_000;
            System.out.println(result);

            long[] durations = new long[10];

            for (int i = 0; i < durations.length; i++) {
                long startParseI = System.nanoTime();
                parseMarkdown.execute(fileContents);
                durations[i] = (System.nanoTime() - startParseI) / 1_000_000;
            }

            System.out.println("\n------------------------------------\n");
            System.out.println("Reading file took:           | " + durationRead + "ms");
            System.out.println("Creating Context took:       | " + durationContext + "ms");
            System.out.println("Parsing markdown took:       | " + durationParse + "ms");
            for (long d : durations) {
                System.out.println("Parsing markdown again took: | " + d + "ms");
            }

            FileOutputStream outputStream = new FileOutputStream("result.html", false);
            byte[] resultToBytes = result.toString().getBytes("UTF-8");
            outputStream.write(resultToBytes);
            outputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
