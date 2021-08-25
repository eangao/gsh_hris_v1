import * as dayjs from 'dayjs';
import {IEmployee} from 'app/entities/employee/employee.model';
import {ILeaveType} from 'app/entities/leave-type/leave-type.model';

export interface ILeave {
  id?: number;
  dateApply?: dayjs.Dayjs;
  dateStart?: dayjs.Dayjs;
  dateEnd?: dayjs.Dayjs;
  dateReturn?: dayjs.Dayjs;
  checkupDate?: dayjs.Dayjs | null;
  convalescingPeriod?: number | null;
  diagnosis?: string | null;
  physician?: string | null;
  employee?: IEmployee | null;
  leaveType?: ILeaveType | null;
}

export class Leave implements ILeave {
  constructor(
    public id?: number,
    public dateApply?: dayjs.Dayjs,
    public dateStart?: dayjs.Dayjs,
    public dateEnd?: dayjs.Dayjs,
    public dateReturn?: dayjs.Dayjs,
    public checkupDate?: dayjs.Dayjs | null,
    public convalescingPeriod?: number | null,
    public diagnosis?: string | null,
    public physician?: string | null,
    public employee?: IEmployee | null,
    public leaveType?: ILeaveType | null
  ) {}
}

export function getLeaveIdentifier(leave: ILeave): number | undefined {
  return leave.id;
}
