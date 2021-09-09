import { IEmployee } from 'app/entities/employee/employee.model';

export interface IEmploymentType {
  id?: number;
  name?: string | null;
  employees?: IEmployee[] | null;
}

export class EmploymentType implements IEmploymentType {
  constructor(public id?: number, public name?: string | null, public employees?: IEmployee[] | null) {}
}

export function getEmploymentTypeIdentifier(employmentType: IEmploymentType): number | undefined {
  return employmentType.id;
}
