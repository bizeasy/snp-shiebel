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

describe('GeoAssoc e2e test', () => {
  const geoAssocPageUrl = '/geo-assoc';
  let username: string;
  let password: string;
  const geoAssocSample = {};

  let geoAssoc;
  let geo;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/geos',
      body: { name: 'as up birdbath', code: 'vast posit', abbreviation: 'delightful' },
    }).then(({ body }) => {
      geo = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/geo-assocs+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/geo-assocs').as('postEntityRequest');
    cy.intercept('DELETE', '/api/geo-assocs/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/geos', {
      statusCode: 200,
      body: [geo],
    });

    cy.intercept('GET', '/api/geo-assoc-types', {
      statusCode: 200,
      body: [],
    });
  });

  afterEach(() => {
    if (geoAssoc) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/geo-assocs/${geoAssoc.id}`,
      }).then(() => {
        geoAssoc = undefined;
      });
    }
  });

  afterEach(() => {
    if (geo) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/geos/${geo.id}`,
      }).then(() => {
        geo = undefined;
      });
    }
  });

  it('GeoAssocs menu should load GeoAssocs page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('geo-assoc');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('GeoAssoc').should('exist');
    cy.location('pathname').should('eq', geoAssocPageUrl);
  });

  describe('GeoAssoc page', () => {
    it('should have translated page title', () => {
      cy.visit(geoAssocPageUrl);
      cy.getEntityHeading('GeoAssoc').should('not.contain', 'snpApp.geoAssoc.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(geoAssocPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create GeoAssoc page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${geoAssocPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('GeoAssoc');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', geoAssocPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/geo-assocs',
          body: {
            ...geoAssocSample,
            geo,
            geoTo: geo,
          },
        }).then(({ body }) => {
          geoAssoc = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/geo-assocs+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/geo-assocs?page=0&size=20>; rel="last",<http://localhost/api/geo-assocs?page=0&size=20>; rel="first"',
              },
              body: [geoAssoc],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(geoAssocPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details GeoAssoc page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('geoAssoc');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', geoAssocPageUrl);
      });

      it('edit button click should load edit GeoAssoc page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('GeoAssoc');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', geoAssocPageUrl);
      });

      it('edit button click should load edit GeoAssoc page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('GeoAssoc');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', geoAssocPageUrl);
      });

      it('last delete button click should delete instance of GeoAssoc', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('geoAssoc').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', geoAssocPageUrl);

        geoAssoc = undefined;
      });
    });
  });

  describe('new GeoAssoc page', () => {
    beforeEach(() => {
      cy.visit(geoAssocPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('GeoAssoc');
    });

    it('should create an instance of GeoAssoc', () => {
      cy.get(`[data-cy="geo"]`).select(1);
      cy.get(`[data-cy="geoTo"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        geoAssoc = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', geoAssocPageUrl);
    });
  });
});
