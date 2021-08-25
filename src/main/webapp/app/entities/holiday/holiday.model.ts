import * as dayjs from 'dayjs';

export interface IHoliday {
  id?: number;
  name?: string | null;
  date?: dayjs.Dayjs | null;
}

export class Holiday implements IHoliday {
  constructor(public id?: number, public name?: string | null, public date?: dayjs.Dayjs | null) {}
}

export function getHolidayIdentifier(holiday: IHoliday): number | undefined {
  return holiday.id;
}
