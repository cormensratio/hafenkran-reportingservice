package de.unipassau.sep19.hafenkran.reportingservice.controller;

import de.unipassau.sep19.hafenkran.reportingservice.dto.ResultDTOList;
import de.unipassau.sep19.hafenkran.reportingservice.service.ResultService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * The REST-Controller for result specific POST- and GET-Endpoints.
 */
@Slf4j
@RestController
@RequestMapping("/results")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ResultController {

    private final ResultService resultService;

    /**
     * GET-Endpoint for receiving the csv- and log-results of an execution with the {@code executionId}.
     *
     * @param executionId The execution to get the results from.
     * @return An {@link de.unipassau.sep19.hafenkran.reportingservice.dto.ResultDTOList} of all results from the execution.
     */
    @GetMapping("/{executionId}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ResultDTOList getResultDTOListOfCSVsByExecutionId(@NonNull @PathVariable UUID executionId) {
        return resultService.retrieveResultDTOListByExecutionId(executionId);
    }

    /**
     * GET-Endpoint for receiving all results (csvs and logs) of the execution with the {@code executionId}.
     *
     * @param executionId The id from the execution to get the results from.
     * @param refreshString A boolean represented as string, which toggles the refresh of the resources.
     * @return A Base64-String with the results.
     */
    @GetMapping(value = "/{executionId}/download", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public byte[] getResultsForExecution(@NonNull @PathVariable UUID executionId, @RequestParam("refresh") String refreshString) {
        return resultService.downloadResultsAsBase64(executionId, refreshString.equals("true"));
    }
}
