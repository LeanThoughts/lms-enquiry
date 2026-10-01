import { Type } from "@angular/core";
import { ListConfig, StageConfig, UpdateConfig, UpdateDialogConfig } from "./generic-config.model";
import { businessPartnerStageConfig } from "./business-partner.config";
import { processEnquiryStageConfig } from "./process-enquiry.config";
import { projectProposalStageConfig } from "./project-proposal.config";
import { iccInprincipleApprovalStageConfig } from "./icc-inprinciple-approval.config";
import { riskAssessmentStageConfig } from "./risk-assessment.config";
import { applicationFeeStageConfig } from "./application-fee.config";
import { boardApprovalStageConfig } from "./board-approval.config";
import { sanctionStageConfig } from "./sanction.config";

const stageConfigs: StageConfig[] = [
    businessPartnerStageConfig,
    processEnquiryStageConfig,
    projectProposalStageConfig,
    iccInprincipleApprovalStageConfig,
    riskAssessmentStageConfig,
    applicationFeeStageConfig,
    boardApprovalStageConfig,
    sanctionStageConfig,
];

/**
 * Merge one kind of configuration from all stages into a single map keyed by entity name
 */
function mergeStageConfigs<T>(kind: 'list' | 'updateDialog' | 'update'): Record<string, T> {
    const merged: Record<string, T> = {};
    stageConfigs.forEach((stageConfig) => {
        Object.entries(stageConfig[kind] ?? {}).forEach(([entity, config]) => {
            if (merged[entity]) {
                throw new Error(`Duplicate ${kind} configuration for entity: ${entity}`);
            }
            merged[entity] = config as T;
        });
    });
    return merged;
}

/**
 * Map every entity of every stage to the stage's service
 */
function buildServiceMap(): Record<string, Type<unknown>> {
    const serviceMap: Record<string, Type<unknown>> = {};
    stageConfigs.forEach((stageConfig) => {
        const entities = [
            ...Object.keys(stageConfig.list ?? {}),
            ...Object.keys(stageConfig.updateDialog ?? {}),
            ...Object.keys(stageConfig.update ?? {}),
        ];
        entities.forEach((entity) => {
            if (serviceMap[entity] && serviceMap[entity] !== stageConfig.service) {
                throw new Error(`Entity ${entity} is configured in more than one stage`);
            }
            serviceMap[entity] = stageConfig.service;
        });
    });
    return serviceMap;
}

export const entityListConfigs = mergeStageConfigs<ListConfig>('list');
export const entityUpdateDialogConfigs = mergeStageConfigs<UpdateDialogConfig>('updateDialog');
export const entityUpdateConfigs = mergeStageConfigs<UpdateConfig>('update');
export const entityServiceMap = buildServiceMap();
