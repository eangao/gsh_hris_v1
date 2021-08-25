export interface IHolidayType {
  id?: number;
  name?: string | null;
}

export class HolidayType implements IHolidayType {
  constructor(public id?: number, public name?: string | null) {}
}

export function getHolidayTypeIdentifier(holidayType: IHolidayType): number | undefined {
  return holidayType.id;
}
