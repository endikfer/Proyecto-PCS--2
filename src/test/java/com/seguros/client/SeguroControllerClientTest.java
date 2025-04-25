package com.seguros.client;

import java.net.http.HttpClient;
import java.net.http.HttpResponse;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class SeguroControllerClientTest {
    @Mock
    private HttpClient httpClientMock;

    @Mock
    private HttpResponse<String> responseMock;

    private SeguroControllerClient clientController;

    @BeforeEach
    @SuppressWarnings("unused")
    void setUp() {
        MockitoAnnotations.openMocks(this);
        clientController = new SeguroControllerClient(httpClientMock, "localhost", "8080");
    }

}
