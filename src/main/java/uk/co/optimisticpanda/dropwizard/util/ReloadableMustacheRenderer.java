package uk.co.optimisticpanda.dropwizard.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.util.Locale;

import javax.ws.rs.WebApplicationException;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheException;
import com.google.common.base.Charsets;
import com.yammer.dropwizard.views.View;
import com.yammer.dropwizard.views.ViewRenderer;

public class ReloadableMustacheRenderer implements ViewRenderer {

    public boolean isRenderable(View view) {
        return view.getTemplateName().endsWith(".mstch");
    }

    public void render(final View view, Locale locale, OutputStream output) throws IOException, WebApplicationException {
        final OutputStreamWriter writer = new OutputStreamWriter(output, Charsets.UTF_8);
        try {
            DefaultMustacheFactory factory = new DefaultMustacheFactory() {

                public Reader getReader(String resourceName) {
                    final InputStream is = view.getClass().getResourceAsStream(resourceName);
                    if (is == null) {
                        throw new MustacheException("Template " + resourceName + " not found");
                    }
                    return new BufferedReader(new InputStreamReader(is, Charsets.UTF_8));
                }
            };

            final Mustache template = factory.compile(view.getTemplateName());
            template.execute(writer, view);
        } finally {
            writer.close();
        }
    }

}
