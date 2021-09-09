jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IEmploymentType, EmploymentType } from '../employment-type.model';
import { EmploymentTypeService } from '../service/employment-type.service';

import { EmploymentTypeRoutingResolveService } from './employment-type-routing-resolve.service';

describe('Service Tests', () => {
  describe('EmploymentType routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: EmploymentTypeRoutingResolveService;
    let service: EmploymentTypeService;
    let resultEmploymentType: IEmploymentType | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(EmploymentTypeRoutingResolveService);
      service = TestBed.inject(EmploymentTypeService);
      resultEmploymentType = undefined;
    });

    describe('resolve', () => {
      it('should return IEmploymentType returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultEmploymentType = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultEmploymentType).toEqual({ id: 123 });
      });

      it('should return new IEmploymentType if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultEmploymentType = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultEmploymentType).toEqual(new EmploymentType());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as EmploymentType })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultEmploymentType = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultEmploymentType).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
