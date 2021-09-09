import { IEmployee } from 'app/entities/employee/employee.model';

export interface IEducation {
  id?: number;
  bachelorDegree?: string | null;
  yearGraduated?: number | null;
  school?: string | null;
  employee?: IEmployee | null;
}

export class Education implements IEducation {
  constructor(
    public id?: number,
    public bachelorDegree?: string | null,
    public yearGraduated?: number | null,
    public school?: string | null,
    public employee?: IEmployee | null
  ) {}
}

export function getEducationIdentifier(education: IEducation): number | undefined {
  return education.id;
}
