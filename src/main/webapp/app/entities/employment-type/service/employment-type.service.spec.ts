import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IEmploymentType, EmploymentType } from '../employment-type.model';

import { EmploymentTypeService } from './employment-type.service';

describe('Service Tests', () => {
  describe('EmploymentType Service', () => {
    let service: EmploymentTypeService;
    let httpMock: HttpTestingController;
    let elemDefault: IEmploymentType;
    let expectedResult: IEmploymentType | IEmploymentType[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(EmploymentTypeService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        name: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a EmploymentType', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new EmploymentType()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a EmploymentType', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a EmploymentType', () => {
        const patchObject = Object.assign({}, new EmploymentType());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of EmploymentType', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a EmploymentType', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addEmploymentTypeToCollectionIfMissing', () => {
        it('should add a EmploymentType to an empty array', () => {
          const employmentType: IEmploymentType = { id: 123 };
          expectedResult = service.addEmploymentTypeToCollectionIfMissing([], employmentType);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(employmentType);
        });

        it('should not add a EmploymentType to an array that contains it', () => {
          const employmentType: IEmploymentType = { id: 123 };
          const employmentTypeCollection: IEmploymentType[] = [
            {
              ...employmentType,
            },
            { id: 456 },
          ];
          expectedResult = service.addEmploymentTypeToCollectionIfMissing(employmentTypeCollection, employmentType);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a EmploymentType to an array that doesn't contain it", () => {
          const employmentType: IEmploymentType = { id: 123 };
          const employmentTypeCollection: IEmploymentType[] = [{ id: 456 }];
          expectedResult = service.addEmploymentTypeToCollectionIfMissing(employmentTypeCollection, employmentType);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(employmentType);
        });

        it('should add only unique EmploymentType to an array', () => {
          const employmentTypeArray: IEmploymentType[] = [{ id: 123 }, { id: 456 }, { id: 41528 }];
          const employmentTypeCollection: IEmploymentType[] = [{ id: 123 }];
          expectedResult = service.addEmploymentTypeToCollectionIfMissing(employmentTypeCollection, ...employmentTypeArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const employmentType: IEmploymentType = { id: 123 };
          const employmentType2: IEmploymentType = { id: 456 };
          expectedResult = service.addEmploymentTypeToCollectionIfMissing([], employmentType, employmentType2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(employmentType);
          expect(expectedResult).toContain(employmentType2);
        });

        it('should accept null and undefined values', () => {
          const employmentType: IEmploymentType = { id: 123 };
          expectedResult = service.addEmploymentTypeToCollectionIfMissing([], null, employmentType, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(employmentType);
        });

        it('should return initial array if no EmploymentType is added', () => {
          const employmentTypeCollection: IEmploymentType[] = [{ id: 123 }];
          expectedResult = service.addEmploymentTypeToCollectionIfMissing(employmentTypeCollection, undefined, null);
          expect(expectedResult).toEqual(employmentTypeCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
