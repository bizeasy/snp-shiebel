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

describe('Geo e2e test', () => {
  const geoPageUrl = '/geo';
  let username: string;
  let password: string;
  const geoSample = { name: 'apropos humble', code: 'mindless o' };

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
    cy.intercept('GET', '/api/geos+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/geos').as('postEntityRequest');
    cy.intercept('DELETE', '/api/geos/*').as('deleteEntityRequest');
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

  it('Geos menu should load Geos page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('geo');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Geo').should('exist');
    cy.location('pathname').should('eq', geoPageUrl);
  });

  describe('Geo page', () => {
    it('should have translated page title', () => {
      cy.visit(geoPageUrl);
      cy.getEntityHeading('Geo').should('not.contain', 'snpApp.geo.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(geoPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Geo page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${geoPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('Geo');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', geoPageUrl);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/geos',
          body: geoSample,
        }).then(({ body }) => {
          geo = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/geos+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/geos?page=0&size=20>; rel="last",<http://localhost/api/geos?page=0&size=20>; rel="first"',
              },
              body: [geo],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(geoPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Geo page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('geo');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', geoPageUrl);
      });

      it('edit button click should load edit Geo page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Geo');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', geoPageUrl);
      });

      it('edit button click should load edit Geo page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Geo');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', geoPageUrl);
      });

      it('last delete button click should delete instance of Geo', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('geo').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', geoPageUrl);

        geo = undefined;
      });
    });
  });

  describe('new Geo page', () => {
    beforeEach(() => {
      cy.visit(geoPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Geo');
    });

    it('should create an instance of Geo', () => {
      cy.get(`[data-cy="name"]`).type('motionless madly');
      cy.get(`[data-cy="name"]`).should('have.value', 'motionless madly');

      cy.get(`[data-cy="code"]`).type('modulo mou');
      cy.get(`[data-cy="code"]`).should('have.value', 'modulo mou');

      cy.get(`[data-cy="abbreviation"]`).type('hm');
      cy.get(`[data-cy="abbreviation"]`).should('have.value', 'hm');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        geo = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', geoPageUrl);
    });
  });
});
