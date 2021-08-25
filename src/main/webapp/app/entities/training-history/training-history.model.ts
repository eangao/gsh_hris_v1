import * as dayjs from 'dayjs';
import {IEmployee} from 'app/entities/employee/employee.model';

export interface ITrainingHistory {
  id?: number;
  trainingName?: string | null;
  trainingDate?: dayjs.Dayjs | null;
  employee?: IEmployee | null;
}

export class TrainingHistory implements ITrainingHistory {
  constructor(
    public id?: number,
    public trainingName?: string | null,
    public trainingDate?: dayjs.Dayjs | null,
    public employee?: IEmployee | null
  ) {}
}

export function getTrainingHistoryIdentifier(trainingHistory: ITrainingHistory): number | undefined {
  return trainingHistory.id;
}
