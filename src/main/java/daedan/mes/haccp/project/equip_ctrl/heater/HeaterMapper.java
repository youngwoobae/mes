package daedan.mes.haccp.project.equip_ctrl.heater;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public interface HeaterMapper {
    void updateRecCcpEvent(Map<String, Object> paramMap);
}
