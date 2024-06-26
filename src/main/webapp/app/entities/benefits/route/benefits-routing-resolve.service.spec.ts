jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IBenefits, Benefits } from '../benefits.model';
import { BenefitsService } from '../service/benefits.service';

import { BenefitsRoutingResolveService } from './benefits-routing-resolve.service';

describe('Service Tests', () => {
  describe('Benefits routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: BenefitsRoutingResolveService;
    let service: BenefitsService;
    let resultBenefits: IBenefits | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(BenefitsRoutingResolveService);
      service = TestBed.inject(BenefitsService);
      resultBenefits = undefined;
    });

    describe('resolve', () => {
      it('should return IBenefits returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultBenefits = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultBenefits).toEqual({ id: 123 });
      });

      it('should return new IBenefits if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultBenefits = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultBenefits).toEqual(new Benefits());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Benefits })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultBenefits = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultBenefits).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
