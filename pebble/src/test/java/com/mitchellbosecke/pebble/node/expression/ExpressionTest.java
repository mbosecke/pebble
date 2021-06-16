package com.mitchellbosecke.pebble.node.expression;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.loader.StringLoader;
import com.mitchellbosecke.pebble.template.PebbleTemplate;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class ExpressionTest {

  private PebbleEngine pebble = new PebbleEngine.Builder().loader(new StringLoader())
      .strictVariables(false).build();

  protected void testExpression(String templateName, String expected) throws IOException {
    PebbleTemplate template = this.pebble.getTemplate(templateName);
    StringWriter writer = new StringWriter();
    template.evaluate(writer);
    assertEquals(expected, writer.toString());
  }

	protected void testExpression(String templateName, String expected, Map<String, Object> context) throws IOException {
		PebbleTemplate template = this.pebble.getTemplate(templateName);
		StringWriter writer = new StringWriter();
		template.evaluate(writer, context);
		assertEquals(expected, writer.toString());
	}

}
