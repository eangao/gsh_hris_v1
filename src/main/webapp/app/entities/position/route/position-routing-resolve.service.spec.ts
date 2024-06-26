jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IPosition, Position } from '../position.model';
import { PositionService } from '../service/position.service';

import { PositionRoutingResolveService } from './position-routing-resolve.service';

describe('Service Tests', () => {
  describe('Position routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: PositionRoutingResolveService;
    let service: PositionService;
    let resultPosition: IPosition | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(PositionRoutingResolveService);
      service = TestBed.inject(PositionService);
      resultPosition = undefined;
    });

    describe('resolve', () => {
      it('should return IPosition returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPosition = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultPosition).toEqual({ id: 123 });
      });

      it('should return new IPosition if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPosition = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultPosition).toEqual(new Position());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Position })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultPosition = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultPosition).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
