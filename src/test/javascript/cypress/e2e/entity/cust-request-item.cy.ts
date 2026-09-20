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

describe('CustRequestItem e2e test', () => {
  const custRequestItemPageUrl = '/cust-request-item';
  let username: string;
  let password: string;
  // const custRequestItemSample = {"quantity":6739.05};

  let custRequestItem;
  // let custRequest;

  before(() => {
    cy.credentials().then(credentials => {
      ({ username, password } = credentials);
    });
  });

  beforeEach(() => {
    cy.login(username, password);
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/cust-requests',
      body: {"name":"extremely","description":"rationalise what between","priority":8,"custRequestDate":"2023-12-14T11:53:29.129Z","responseRequiredDate":"2023-12-14T11:54:39.550Z","openDateTime":"2023-12-13T23:13:17.355Z","closedDateTime":"2023-12-14T20:36:24.374Z","internalComment":"except elevator","billed":false,"custEstimatedDurationDays":24220},
    }).then(({ body }) => {
      custRequest = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/cust-request-items+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/cust-request-items').as('postEntityRequest');
    cy.intercept('DELETE', '/api/cust-request-items/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/cust-requests', {
      statusCode: 200,
      body: [custRequest],
    });

    cy.intercept('GET', '/api/statuses', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/products', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/uoms', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/shopping-list-items', {
      statusCode: 200,
      body: [],
    });

  });
   */

  afterEach(() => {
    if (custRequestItem) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/cust-request-items/${custRequestItem.id}`,
      }).then(() => {
        custRequestItem = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (custRequest) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/cust-requests/${custRequest.id}`,
      }).then(() => {
        custRequest = undefined;
      });
    }
  });
   */

  it('CustRequestItems menu should load CustRequestItems page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('cust-request-item');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CustRequestItem').should('exist');
    cy.location('pathname').should('eq', custRequestItemPageUrl);
  });

  describe('CustRequestItem page', () => {
    it('should have translated page title', () => {
      cy.visit(custRequestItemPageUrl);
      cy.getEntityHeading('CustRequestItem').should('not.contain', 'snpApp.custRequestItem.home.title');
    });

    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(custRequestItemPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CustRequestItem page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.location('pathname').should('eq', `${custRequestItemPageUrl}/new`);
        cy.getEntityCreateUpdateHeading('CustRequestItem');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', custRequestItemPageUrl);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/cust-request-items',
          body: {
            ...custRequestItemSample,
            custRequest: custRequest,
          },
        }).then(({ body }) => {
          custRequestItem = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/cust-request-items+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/cust-request-items?page=0&size=20>; rel="last",<http://localhost/api/cust-request-items?page=0&size=20>; rel="first"',
              },
              body: [custRequestItem],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(custRequestItemPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(custRequestItemPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response?.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details CustRequestItem page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('custRequestItem');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', custRequestItemPageUrl);
      });

      it('edit button click should load edit CustRequestItem page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CustRequestItem');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', custRequestItemPageUrl);
      });

      it('edit button click should load edit CustRequestItem page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CustRequestItem');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', custRequestItemPageUrl);
      });

      // Reason: cannot create a required entity with relationship with required relationships.
      it.skip('last delete button click should delete instance of CustRequestItem', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('custRequestItem').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.location('pathname').should('eq', custRequestItemPageUrl);

        custRequestItem = undefined;
      });
    });
  });

  describe('new CustRequestItem page', () => {
    beforeEach(() => {
      cy.visit(custRequestItemPageUrl);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('CustRequestItem');
    });

    // Reason: cannot create a required entity with relationship with required relationships.
    it.skip('should create an instance of CustRequestItem', () => {
      cy.get(`[data-cy="sequenceNo"]`).type('13196');
      cy.get(`[data-cy="sequenceNo"]`).should('have.value', '13196');

      cy.get(`[data-cy="priority"]`).type('3');
      cy.get(`[data-cy="priority"]`).should('have.value', '3');

      cy.get(`[data-cy="requiredByDate"]`).type('2023-12-14T03:33');
      cy.get(`[data-cy="requiredByDate"]`).blur();
      cy.get(`[data-cy="requiredByDate"]`).should('have.value', '2023-12-14T03:33');

      cy.get(`[data-cy="quantity"]`).type('472.96');
      cy.get(`[data-cy="quantity"]`).should('have.value', '472.96');

      cy.get(`[data-cy="selectedAmount"]`).type('28274.62');
      cy.get(`[data-cy="selectedAmount"]`).should('have.value', '28274.62');

      cy.get(`[data-cy="maxAmount"]`).type('23970.8');
      cy.get(`[data-cy="maxAmount"]`).should('have.value', '23970.8');

      cy.get(`[data-cy="description"]`).type('avalanche');
      cy.get(`[data-cy="description"]`).should('have.value', 'avalanche');

      cy.get(`[data-cy="story"]`).type('nor');
      cy.get(`[data-cy="story"]`).should('have.value', 'nor');

      cy.get(`[data-cy="custRequest"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        custRequestItem = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.location('pathname').should('eq', custRequestItemPageUrl);
    });
  });
});
