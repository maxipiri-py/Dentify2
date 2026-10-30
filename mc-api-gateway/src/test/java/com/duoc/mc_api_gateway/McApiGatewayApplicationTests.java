package com.duoc.mc_api_gateway;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.gateway.server.mvc.config.GatewayMvcProperties;
import org.springframework.cloud.gateway.server.mvc.config.RouteProperties;

@SpringBootTest
class McApiGatewayApplicationTests {

	@Autowired
	private GatewayMvcProperties gatewayProperties;

	@Test
	void contextLoads() {
	}

	@Test
	void rutasSeCarganDesdeConfiguracion() {
		List<String> ids = gatewayProperties.getRoutes().stream().map(RouteProperties::getId).toList();

		assertThat(ids).containsExactlyInAnyOrder("mc-agenda", "mc-dentista", "mc-login", "mc-notificaciones",
				"mc-observaciones", "mc-paciente", "mc-pagos", "mc-prestaciones", "mc-presupuesto", "mc-reporte");
	}

	@Test
	void rutaDentistaApuntaAlPathDelControlador() {
		RouteProperties dentista = gatewayProperties.getRoutes().stream()
				.filter(r -> r.getId().equals("mc-dentista"))
				.findFirst()
				.orElseThrow();

		assertThat(dentista.getUri()).hasToString("http://localhost:8082");
		assertThat(dentista.getPredicates()).singleElement()
				.satisfies(p -> assertThat(p.getArgs().values()).containsExactly("/api/dentista/**"));
	}

}
