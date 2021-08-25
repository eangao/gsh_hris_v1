import {IEmployee} from 'app/entities/employee/employee.model';

export interface IDependents {
  id?: number;
  firstName?: string | null;
  lastName?: string | null;
  relation?: string | null;
  employee?: IEmployee | null;
}

export class Dependents implements IDependents {
  constructor(
    public id?: number,
    public firstName?: string | null,
    public lastName?: string | null,
    public relation?: string | null,
    public employee?: IEmployee | null
  ) {}
}

export function getDependentsIdentifier(dependents: IDependents): number | undefined {
  return dependents.id;
}
