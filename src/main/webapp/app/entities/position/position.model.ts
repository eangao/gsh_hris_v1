import {IEmployee} from 'app/entities/employee/employee.model';

export interface IPosition {
  id?: number;
  name?: string | null;
  employee?: IEmployee;
}

export class Position implements IPosition {
  constructor(public id?: number, public name?: string | null, public employee?: IEmployee) {}
}

export function getPositionIdentifier(position: IPosition): number | undefined {
  return position.id;
}
