package com.owiseman.dataapi.service;

import com.owiseman.dataapi.repository.SysWorkflowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysWorkflowService {
    @Autowired
    private SysWorkflowRepository sysWorkflowRepository;


}
