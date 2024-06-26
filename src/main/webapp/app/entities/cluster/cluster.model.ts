import { IDepartment } from 'app/entities/department/department.model';

export interface ICluster {
  id?: number;
  name?: string | null;
  departments?: IDepartment[] | null;
}

export class Cluster implements ICluster {
  constructor(public id?: number, public name?: string | null, public departments?: IDepartment[] | null) {}
}

export function getClusterIdentifier(cluster: ICluster): number | undefined {
  return cluster.id;
}
