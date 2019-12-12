package de.unipassau.sep19.hafenkran.reportingservice.controller;

import de.unipassau.sep19.hafenkran.reportingservice.dto.ResultDTO;
import de.unipassau.sep19.hafenkran.reportingservice.service.ResultService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/getResults")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ResultController {

    private final ResultService resultService;

    @GetMapping("/{executionId}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public List<ResultDTO> getResultDTOListByExecutionId(@NonNull @PathVariable UUID executionId) {
        return resultService.retrieveResultDTOListByExecutionId(executionId);
    }

}
