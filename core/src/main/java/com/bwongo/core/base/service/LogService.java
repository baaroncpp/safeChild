package com.bwongo.core.base.service;

import com.bwongo.commons.models.utils.Validate;
import com.bwongo.core.base.model.jpa.TLog;
import com.bwongo.core.base.repository.TLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.bwongo.core.base.model.dto.LogResponseDto;

import static com.bwongo.commons.models.utils.DateTimeUtil.stringToDate;
import static com.bwongo.core.base.utils.BasicMsgConstants.DATE_TIME_FORMAT;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author bkaaron
 * @Project nc
 * @Date 12/8/23
 **/
@Service
@RequiredArgsConstructor
@Slf4j
public class LogService {
    private final TLogRepository logRepository;
    private final AuditService auditService;
    private final BaseDtoService baseDtoService;

    public void recordLog(TLog log){
        auditService.stampLongEntity(log);
        logRepository.save(log);
    }

    public List<LogResponseDto> getAllLogs(Pageable pageable){
        return logRepository.findAll(pageable).getContent().stream()
                .map(baseDtoService::logToDto)
                .collect(Collectors.toList());
    }

    public List<LogResponseDto> getLogsByDate(Pageable pageable, String date){

        Validate.isAcceptableDateFormat(this, date);
        stringToDate(date, DATE_TIME_FORMAT);

        return logRepository.findAllByCreatedOn(stringToDate(date, DATE_TIME_FORMAT), pageable).stream()
                .map(baseDtoService::logToDto)
                .collect(Collectors.toList());
    }
}
