/*******************************************************************************
 * This file is part of Pebble.
 *
 * Copyright (c) 2014 by Mitchell Bösecke
 *
 * For the full copyright and license information, please view the LICENSE
 * file that was distributed with this source code.
 ******************************************************************************/
package com.mitchellbosecke.pebble.extension.core;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.extension.*;
import com.mitchellbosecke.pebble.node.expression.*;
import com.mitchellbosecke.pebble.operator.*;
import com.mitchellbosecke.pebble.tokenParser.TokenParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CoreExtension extends AbstractExtension {

    private final List<TokenParser> tokenParsers;

    private CoreExtension(List<TokenParser> tokenParsers){
        this.tokenParsers = tokenParsers;
    }

    @Override
    public List<TokenParser> getTokenParsers() {
        return tokenParsers;
    }

    @Override
    public List<UnaryOperator> getUnaryOperators() {
        ArrayList<UnaryOperator> operators = new ArrayList<>();
        operators.add(new UnaryOperatorImpl("not", 5, UnaryNotExpression.class));
        operators.add(new UnaryOperatorImpl("+", 500, UnaryPlusExpression.class));
        operators.add(new UnaryOperatorImpl("-", 500, UnaryMinusExpression.class));
        return operators;
    }

    @Override
    public List<BinaryOperator> getBinaryOperators() {
        ArrayList<BinaryOperator> operators = new ArrayList<>();
        operators.add(new BinaryOperatorImpl("or", 10, OrExpression.class, Associativity.LEFT));
        operators.add(new BinaryOperatorImpl("and", 15, AndExpression.class, Associativity.LEFT));
        operators.add(new BinaryOperatorImpl("is", 20, PositiveTestExpression.class, Associativity.LEFT));
        operators.add(new BinaryOperatorImpl("is not", 20, NegativeTestExpression.class, Associativity.LEFT));
        operators.add(new BinaryOperatorImpl("contains", 20, ContainsExpression.class, Associativity.LEFT));
        operators.add(new BinaryOperatorImpl("==", 30, EqualsExpression.class, Associativity.LEFT));
        operators.add(new BinaryOperatorImpl("equals", 30, EqualsExpression.class, Associativity.LEFT));
        operators.add(new BinaryOperatorImpl("!=", 30, NotEqualsExpression.class, Associativity.LEFT));
        operators.add(new BinaryOperatorImpl(">", 30, GreaterThanExpression.class, Associativity.LEFT));
        operators.add(new BinaryOperatorImpl("<", 30, LessThanExpression.class, Associativity.LEFT));
        operators.add(new BinaryOperatorImpl(">=", 30, GreaterThanEqualsExpression.class, Associativity.LEFT));
        operators.add(new BinaryOperatorImpl("<=", 30, LessThanEqualsExpression.class, Associativity.LEFT));
        operators.add(new BinaryOperatorImpl("+", 40, AddExpression.class, Associativity.LEFT));
        operators.add(new BinaryOperatorImpl("-", 40, SubtractExpression.class, Associativity.LEFT));
        operators.add(new BinaryOperatorImpl("*", 60, MultiplyExpression.class, Associativity.LEFT));
        operators.add(new BinaryOperatorImpl("/", 60, DivideExpression.class, Associativity.LEFT));
        operators.add(new BinaryOperatorImpl("%", 60, ModulusExpression.class, Associativity.LEFT));
        operators.add(new BinaryOperatorImpl("|", 100, FilterExpression.class, Associativity.LEFT));
        operators.add(new BinaryOperatorImpl("~", 110, ConcatenateExpression.class, Associativity.LEFT));
        operators.add(new BinaryOperatorImpl("..", 120, RangeExpression.class, Associativity.LEFT));

        return operators;
    }

    @Override
    public Map<String, Filter> getFilters() {
        Map<String, Filter> filters = new HashMap<>();
        filters.put("abbreviate", new AbbreviateFilter());
        filters.put("abs", new AbsFilter());
        filters.put("capitalize", new CapitalizeFilter());
        filters.put("date", new DateFilter());
        filters.put("default", new DefaultFilter());
        filters.put("first", new FirstFilter());
        filters.put("join", new JoinFilter());
        filters.put("last", new LastFilter());
        filters.put("lower", new LowerFilter());
        filters.put("numberformat", new NumberFormatFilter());
        filters.put("slice", new SliceFilter());
        filters.put("sort", new SortFilter());
        filters.put("rsort", new RsortFilter());
        filters.put("title", new TitleFilter());
        filters.put("trim", new TrimFilter());
        filters.put("upper", new UpperFilter());
        filters.put("urlencode", new UrlEncoderFilter());
        filters.put("length", new LengthFilter());
        filters.put(ReplaceFilter.FILTER_NAME, new ReplaceFilter());
        filters.put(MergeFilter.FILTER_NAME, new MergeFilter());
        return filters;
    }

    @Override
    public Map<String, Test> getTests() {
        Map<String, Test> tests = new HashMap<>();
        tests.put("empty", new EmptyTest());
        tests.put("even", new EvenTest());
        tests.put("iterable", new IterableTest());
        tests.put("map", new MapTest());
        tests.put("null", new NullTest());
        tests.put("odd", new OddTest());
        tests.put("defined", new DefinedTest());
        return tests;
    }

    @Override
    public Map<String, Function> getFunctions() {
        Map<String, Function> functions = new HashMap<>();

        /*
         * For efficiency purposes, some core functions are individually parsed
         * by our expression parser and compiled in their own unique way. This
         * includes the block and parent functions.
         */

        functions.put("max", new MaxFunction());
        functions.put("min", new MinFunction());
        functions.put(RangeFunction.FUNCTION_NAME, new RangeFunction());
        return functions;
    }

    @Override
    public Map<String, Object> getGlobalVariables() {
        return null;
    }

    @Override
    public List<NodeVisitorFactory> getNodeVisitors() {
        List<NodeVisitorFactory> visitors = new ArrayList<>();
        visitors.add(new MacroAndBlockRegistrantNodeVisitorFactory());
        return visitors;
    }

    /**
     * This {@link Builder} is used to enable/disable the default CoreExtensions
     */
    public static class Builder extends ChainableBuilder<PebbleEngine.Builder> {

        private boolean enabled = true;

        private TokenParserBuilder tokenParserBuilder;

        /**
         * @param builder an instance of {@link PebbleEngine.Builder} that will be returned
         *                when calling Builder{@link #and()}
         */
        public Builder(PebbleEngine.Builder builder){
            super(builder);
        }

        /**
         * this method enables any {@link CoreExtension} functionality
         *
         * @return the {@link Builder} itself
         */
        public Builder enable(){
            enabled = true;
            return this;
        }

        /**
         * this method disables any {@link CoreExtension} functionality
         *
         * @return the {@link Builder} itself
         */
        public Builder disable(){
            enabled = false;
            return this;
        }

        /**
         * retrieve a {@link TokenParserBuilder} to customize which TokenParser will be configured
         *
         * @return a {@link TokenParserBuilder} to work with
         */
        public TokenParserBuilder tokenParsers() {
            if(tokenParserBuilder == null) {
                tokenParserBuilder = new TokenParserBuilder(this);
            }
            return tokenParserBuilder;
        }

        /**
         * this methods builds the {@link CoreExtension} according to the
         * configuration. If the extension was disabled it'll return an
         * instance of {@link NoOpExtension}.
         *
         * @return either an {@link CoreExtension} or a {@link NoOpExtension}
         */
        public Extension build(){
            if(enabled){
                return new CoreExtension(
                        tokenParsers().build()
                );
            }else{
                return new NoOpExtension();
            }
        }

    }

}
