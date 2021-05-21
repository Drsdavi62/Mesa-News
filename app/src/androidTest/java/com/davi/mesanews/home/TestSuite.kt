package com.davi.mesanews.home

import org.junit.runner.RunWith
import org.junit.runners.Suite

@RunWith(Suite::class)
@Suite.SuiteClasses(
    LoginFragmentTest::class,
    NewsFragmentTest::class
)
class TestSuite