import {IEmployee} from 'app/entities/employee/employee.model';

export interface IBenefits {
  id?: number;
  name?: string | null;
  employee?: IEmployee | null;
}

export class Benefits implements IBenefits {
  constructor(public id?: number, public name?: string | null, public employee?: IEmployee | null) {}
}

export function getBenefitsIdentifier(benefits: IBenefits): number | undefined {
  return benefits.id;
}
