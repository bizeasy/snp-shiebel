import {
  entityConfirmDeleteButtonSelector,
  entityCreateButtonSelector,
  entityCreateCancelButtonSelector,
  entityCreateSaveButtonSelector,
  entityDeleteButtonSelector,
  entityDetailsBackButtonSelector,
  entityDetailsButtonSelector,
  entityEditButtonSelector,
  entityTableSelector,
} from '../../support/entity';

describe('GeoPoint e2e test', () => {
  const geoPointPageUrl = '/geo-point';
  let username: string;
  let password: string;
  const geoPointSample = {};

  let geoPoint;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/geo-points+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/geo-points').as('postEntityRequest');
    cy.intercept('DELETE', '/api/geo-points/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (geoPoint) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/geo-points/${geoPoint.id}`,
      }).then(() => {
        geoPoint = undefined;
      });
    }
  });

  it('GeoPoints menu should load GeoPoints page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('geo-point');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('GeoPoint').should('exist');
    cy.location('pathname').should('eq', geoPointPageUrl);
  });

  describe('GeoPoint page', () => {
    it('should have translated page title', () => {
      cy.visit(geoPointPageUrl);
      cy.getEntityHeading('GeoPoint').should('not.contain', 'snpApp.geoPoint.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(geoPointPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create GeoPoint page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${geoPointPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('GeoPoint');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', geoPointPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/geo-points',
          body: geoPointSample,
        }).then(({ body }) => {
          geoPoint = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/geo-points+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [geoPoint],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(geoPointPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details GeoPoint page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('geoPoint');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', geoPointPageUrl);
      });

      it('edit button click should load edit GeoPoint page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('GeoPoint');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', geoPointPageUrl);
      });

      it('edit button click should load edit GeoPoint page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('GeoPoint');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', geoPointPageUrl);
      });

      it('last delete button click should delete instance of GeoPoint', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('geoPoint').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', geoPointPageUrl);

        geoPoint = undefined;
      });
    });
  });

  describe('new GeoPoint page', () => {
    beforeEach(() => {
      cy.visit(geoPointPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('GeoPoint');
    });

    it('should create an instance of GeoPoint', () => {
      cy.get(`[data-cy="latitude"]`).type('10140.37');
      cy.get(`[data-cy="latitude"]`).should('have.value', '10140.37');

      cy.get(`[data-cy="longitude"]`).type('17185.02');
      cy.get(`[data-cy="longitude"]`).should('have.value', '17185.02');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        geoPoint = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', geoPointPageUrl);
    });
  });
});
